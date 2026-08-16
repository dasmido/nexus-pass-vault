<script>
  import { onMount } from "svelte";
  import {
    Button,
    ComposedModal,
    DataTable,
    InlineNotification,
    ModalBody,
    ModalFooter,
    ModalHeader,
    TextInput,
    Toolbar,
    ToolbarContent,
  } from "carbon-components-svelte";
  import Add from "carbon-icons-svelte/lib/Add.svelte";
  import Locked from "carbon-icons-svelte/lib/Locked.svelte";
  import { createPassword, getPasswords } from "../services/passwordService.js";

  let passwords = [];
  let error = "";
  let isUnlockDialogOpen = false;
  let pinCode = "";
  let pinError = "";
  let selectedPassword = null;
  let revealedPasswordId = null;

  let isCreateDialogOpen = false;
  let newPassword = { site: "", username: "", secret: "" };
  let createError = "";
  let isSaving = false;

  const vaultPin = "1234";

  onMount(async () => {
    try {
      passwords = await getPasswords();
    } catch (requestError) {
      error = requestError instanceof Error ? requestError.message : "Unable to load passwords.";
    }
  });

  function openUnlockDialog(password) {
    selectedPassword = password;
    pinCode = "";
    pinError = "";
    isUnlockDialogOpen = true;
  }

  function unlockSecret() {
    if (pinCode !== vaultPin) {
      pinError = "Incorrect PIN.";
      return;
    }

    revealedPasswordId = selectedPassword.id;
    isUnlockDialogOpen = false;
  }

  function closeUnlockDialog() {
    isUnlockDialogOpen = false;
    pinCode = "";
    pinError = "";
  }

  function openCreateDialog() {
    newPassword = { site: "", username: "", secret: "" };
    createError = "";
    isCreateDialogOpen = true;
  }

  function closeCreateDialog() {
    isCreateDialogOpen = false;
    createError = "";
  }

  async function savePassword() {
    if (!newPassword.site.trim() || !newPassword.username.trim() || !newPassword.secret.trim()) {
      createError = "Site, username and secret are required.";
      isCreateDialogOpen = true;
      return;
    }

    isSaving = true;
    try {
      const created = await createPassword({
        site: newPassword.site.trim(),
        username: newPassword.username.trim(),
        secret: newPassword.secret.trim(),
      });
      passwords = [...passwords, created];
      isCreateDialogOpen = false;
      createError = "";
    } catch (requestError) {
      createError = requestError instanceof Error ? requestError.message : "Unable to save password.";
      isCreateDialogOpen = true;
    } finally {
      isSaving = false;
    }
  }
</script>

{#if error}
  <p>{error}</p>
{:else}
  <DataTable
    headers={[
      { key: "site", value: "Site" },
      { key: "username", value: "Username" },
      { key: "secret", value: "Secret" },
      { key: "actions", value: "Actions" },
    ]}
    rows={passwords}
  >
    <Toolbar>
      <ToolbarContent>
        <Button icon={Add} on:click={openCreateDialog}>New password</Button>
      </ToolbarContent>
    </Toolbar>
    <svelte:fragment slot="cell" let:cell let:row>
      {#if cell.key === "secret"}
        {#if revealedPasswordId === row.id}
          {cell.value}
        {:else}
          ********
        {/if}
      {:else if cell.key === "actions"}
        <Button
          kind="ghost"
          size="sm"
          icon={Locked}
          hasIconOnly
          tooltipPosition="left"
          iconDescription="Unlock secret"
          on:click={() => openUnlockDialog(row)}
        />
      {:else}
        {cell.value}
      {/if}
    </svelte:fragment>
  </DataTable>
{/if}

<ComposedModal bind:open={isUnlockDialogOpen} on:close={closeUnlockDialog} on:submit={unlockSecret}>
  <ModalHeader label="Vault protection" title="Enter PIN to reveal secret" />
  <ModalBody hasForm>
    <TextInput
      id="password-unlock-pin"
      labelText="PIN code"
      type="password"
      inputmode="numeric"
      maxlength={8}
      bind:value={pinCode}
      invalid={Boolean(pinError)}
      invalidText={pinError}
      data-modal-primary-focus
    />
  </ModalBody>
  <ModalFooter primaryButtonText="Reveal" secondaryButtonText="Cancel" />
</ComposedModal>

<ComposedModal bind:open={isCreateDialogOpen} on:close={closeCreateDialog} on:submit={savePassword}>
  <ModalHeader label="Vault" title="Add a new password" />
  <ModalBody hasForm>
    {#if createError}
      <InlineNotification kind="error" hideCloseButton title="Error" subtitle={createError} />
    {/if}
    <TextInput
      id="new-password-site"
      labelText="Site"
      placeholder="example.com"
      bind:value={newPassword.site}
      data-modal-primary-focus
    />
    <TextInput id="new-password-username" labelText="Username" bind:value={newPassword.username} />
    <TextInput id="new-password-secret" labelText="Secret" type="password" bind:value={newPassword.secret} />
  </ModalBody>
  <ModalFooter
    primaryButtonText={isSaving ? "Saving..." : "Save"}
    primaryButtonDisabled={isSaving}
    secondaryButtonText="Cancel"
  />
</ComposedModal>
