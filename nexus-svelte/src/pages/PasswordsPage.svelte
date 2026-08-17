<script>
  import { onMount } from "svelte";
  import {
    Button,
    ComposedModal,
    DataTable,
    InlineLoading,
    InlineNotification,
    ModalBody,
    ModalFooter,
    ModalHeader,
    TextInput,
    Toolbar,
    ToolbarContent,
    Grid,
    Stack,
  } from "carbon-components-svelte";
  import Add from "carbon-icons-svelte/lib/Add.svelte";
  import Edit from "carbon-icons-svelte/lib/Edit.svelte";
  import TrashCan from "carbon-icons-svelte/lib/TrashCan.svelte";
  import Locked from "carbon-icons-svelte/lib/Locked.svelte";
  import Copy from "carbon-icons-svelte/lib/Copy.svelte";
  import Launch from "carbon-icons-svelte/lib/Launch.svelte";
  import { createPassword, getPasswords, updatePassword, deletePassword } from "../services/passwordService.js";

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

  let isEditDialogOpen = false;
  let editingPassword = null;
  let editFormData = { site: "", username: "", secret: "" };
  let editError = "";

  let isDeleteConfirmOpen = false;
  let passwordToDelete = null;
  let deleteError = "";
  let isDeleting = false;

  let page = 0;
  let hasMore = true;
  let isLoadingMore = false;
  let searchQuery = "";

//TODO: In a real application, the vault PIN should be securely stored and managed, not hardcoded in the frontend code.
  const vaultPin = "1234";


  $: filteredPasswords = passwords.filter(p => 
    p.site.toLowerCase().includes(searchQuery.toLowerCase()) ||
    p.username.toLowerCase().includes(searchQuery.toLowerCase())
  );

  async function loadNextPage() {
    if (!hasMore || isLoadingMore) return;
    isLoadingMore = true;
    try {
      const result = await getPasswords(page, 20);
      passwords = [...passwords, ...result.content];
      hasMore = !result.last;
      page += 1;
    } catch (requestError) {
      error = requestError instanceof Error ? requestError.message : "Unable to load passwords.";
    } finally {
      isLoadingMore = false;
    }
  }

  onMount(() => {
    loadNextPage();
  });

  function observeSentinel(node) {
    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) loadNextPage();
    });
    observer.observe(node);
    return { destroy: () => observer.disconnect() };
  }

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


  function openEditDialog(password) {
    editingPassword = password;
    editFormData = { ...password };
    editError = "";
    isEditDialogOpen = true;
  }

  function closeEditDialog() {
    isEditDialogOpen = false;
    editError = "";
  }

  async function savePasswordChanges() {
    if (!editFormData.site.trim() || !editFormData.username.trim() || !editFormData.secret.trim()) {
      editError = "Site, username and secret are required.";
      return;
    }

    try {
      const updated = await updatePassword(editingPassword.id, {
        site: editFormData.site.trim(),
        username: editFormData.username.trim(),
        secret: editFormData.secret.trim(),
      });

      passwords = passwords.map((p) => (p.id === updated.id ? updated : p));
      isEditDialogOpen = false;

    } catch (requestError) {
      editError = requestError instanceof Error ? requestError.message : "Unable to update password.";
    } 
  }

  function openDeleteConfirm(password) {
  passwordToDelete = password;
  deleteError = "";
  isDeleteConfirmOpen = true;
}

function closeDeleteConfirm() {
  isDeleteConfirmOpen = false;
}

async function confirmDelete() {
  isDeleting = true;
  try {
    await deletePassword(passwordToDelete.id);
    passwords = passwords.filter((p) => p.id !== passwordToDelete.id);
    isDeleteConfirmOpen = false;
  } catch (requestError) {
    deleteError = requestError instanceof Error ? requestError.message : "Unable to delete password.";
  } finally {
    isDeleting = false;
  }
}

async function copyToClipboard(text) {
  try {
    await navigator.clipboard.writeText(text);
  } catch (err) {
    error = "Unable to copy to clipboard: " + (err instanceof Error ? err.message : String(err));
  }
}

function openSite(site) {
    const url = /^https?:\/\//i.test(site) ? site : `https://${site}`;
    const website = new URL(url);

    if (!["http:", "https:"].includes(website.protocol)) {
      throw new Error("Only HTTP and HTTPS websites can be opened.");
    }

    window.open(website.href, "_blank", "noopener,noreferrer");
  }
</script>

<style>
  :global(.bx--btn.passwords-header) {
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10rem;
  }

  :global(.passwords-header) {
    align-items: center;
    justify-content: space-between;
    padding-bottom: 2rem;
    padding-top: 2rem;
  }

  :global(.bx--toolbar-content ) {
    background-color: white;
  }

  :global(.cell-with-action) {
    display: inline-flex;
    align-items: center;
    gap: 0.25rem;
  }

  </style>

<Grid fullWidth>
<!--{#if error}
  <p>{error}</p>
{:else}
-->

  <Stack orientation="horizontal" gap="3rem" class="passwords-header">
  <h1>Passwords</h1>

  <Toolbar>
    <ToolbarContent>
    <TextInput
      id="search-passwords"
      labelText="Search"
      placeholder="Search by site or username..."
      bind:value={searchQuery}
    />
      <Button class="new-password-button" icon={Add} on:click={openCreateDialog}>
      </Button>
    </ToolbarContent>
  </Toolbar>
</Stack>
{#if filteredPasswords.length === 0}
  <div style="display: flex; justify-content: center; align-items: center; height: 300px;">
    <p>No passwords found</p>
  </div>
{:else}
  <DataTable
    headers={[
      { key: "site", value: "Website" },
      { key: "username", value: "Username" },
      { key: "secret", value: "Secret" },
      { key: "actions", value: "Actions" },
    ]}
    rows={filteredPasswords}
  >


  
    <svelte:fragment slot="cell" let:cell let:row>
  {#if cell.key === "site"}
    <span class="cell-with-action">
      {cell.value}
      <Button
        kind="ghost"
        size="sm"
        icon={Launch}
        hasIconOnly
        tooltipPosition="right"
        iconDescription="Open website"
        on:click={() => openSite(cell.value)}
      />
    </span>
  {:else if cell.key === "username"}
    <span class="cell-with-action">
      {cell.value}
      <Button
        kind="ghost"
        size="sm"
        icon={Copy}
        hasIconOnly
        tooltipPosition="right"
        iconDescription="Copy username"
        on:click={() => copyToClipboard(cell.value)}
      />
    </span>
  {:else if cell.key === "secret"}
    {#if revealedPasswordId === row.id}
      <span class="cell-with-action">
        {cell.value}
        <Button
          kind="ghost"
          size="sm"
          icon={Copy}
          hasIconOnly
          tooltipPosition="right"
          iconDescription="Copy secret"
          on:click={() => copyToClipboard(cell.value)}
        />
      </span>
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
        <Button
          kind="ghost"
          size="sm"
          icon={Edit}
          hasIconOnly
          tooltipPosition="left"
          iconDescription="Edit password"
          on:click={() => openEditDialog(row)}
        />
        <Button
          kind="ghost"
          size="sm"
          icon={TrashCan}
          hasIconOnly
          tooltipPosition="left"
          iconDescription="Delete password"
          on:click={() => openDeleteConfirm(row)}
        />
  {:else}
    {cell.value}
  {/if}
</svelte:fragment>
  </DataTable>
  {/if}
  {#if isLoadingMore}
    <InlineLoading description="Loading more passwords..." />
  {/if}
  {#if hasMore}
    <div use:observeSentinel style="height: 1px;"></div>
  {/if}
<!--{/if}-->
</Grid>

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

<!-- Edit Dialog -->
<ComposedModal bind:open={isEditDialogOpen} on:close={closeEditDialog} on:submit={savePasswordChanges}>
  <ModalHeader label="Vault" title="Edit password" />
  <ModalBody hasForm>
    {#if editError}
      <InlineNotification kind="error" hideCloseButton title="Error" subtitle={editError} />
    {/if}
    <TextInput
      id="edit-password-site"
      labelText="Site"
      bind:value={editFormData.site}
      data-modal-primary-focus
    />
    <TextInput
      id="edit-password-username"
      labelText="Username"
      bind:value={editFormData.username}
    />
    <TextInput
      id="edit-password-secret"
      labelText="Secret"
      type="password"
      bind:value={editFormData.secret}
    />
  </ModalBody>
  <ModalFooter
    primaryButtonText="Save"
    secondaryButtonText="Cancel"
  />
</ComposedModal>

<!-- Delete Confirmation Dialog -->
<ComposedModal bind:open={isDeleteConfirmOpen} on:close={closeDeleteConfirm} on:submit={confirmDelete}>
  <ModalHeader label="Vault" title="Confirm deletion" />
  <ModalBody hasForm>
    {#if deleteError}
      <InlineNotification kind="error" hideCloseButton title="Error" subtitle={deleteError} />
    {/if}
    <p>Are you sure you want to delete the password for <strong>{passwordToDelete?.site}</strong>?</p>
    <p style="color: #da1e28; margin-top: 1rem;">This action cannot be undone.</p>
  </ModalBody>
  <ModalFooter
    primaryButtonText={isDeleting ? "Deleting..." : "Delete"}
    primaryButtonDisabled={isDeleting}
    secondaryButtonText="Cancel"
    danger={true}
  />
</ComposedModal>
