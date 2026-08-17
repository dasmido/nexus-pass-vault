<script>
import { onMount } from "svelte";
  import {
    Column,
    Content,
    Grid,
    Header,
    HeaderAction,
    HeaderPanelDivider,
    HeaderPanelLink,
    HeaderPanelLinks,
    HeaderUtilities,
    Row,
    SkipToContent,
  } from "carbon-components-svelte";
  import Help from "carbon-icons-svelte/lib/Help.svelte";
  import Notification from "carbon-icons-svelte/lib/Notification.svelte";
  import Locked from "carbon-icons-svelte/lib/Locked.svelte";
  import Settings from "carbon-icons-svelte/lib/Settings.svelte";
 import UserAvatar from "carbon-icons-svelte/lib/UserAvatar.svelte";
 import LogoGithub from "carbon-icons-svelte/lib/LogoGithub.svelte";

  import PasswordsPage from "./pages/PasswordsPage.svelte";
  import SettingsPage from "./pages/SettingsPage.svelte";


  let currentRoute = "/dashboard";
  let isOpen1 = false;
  let isOpen2 = false;
  let isOpen3 = false;


    onMount(() => {
    currentRoute = window.location.pathname;
    
    // Redirect root path to /passwords
    if (currentRoute === "/") {
      window.location.href = "/passwords";
    }
  });
</script>

<Header companyName="Nexus" platformName="Pass Vault" isSideNavOpen>
  <svelte:fragment slot="skipToContent"><SkipToContent /></svelte:fragment>
  <HeaderUtilities>
    
    <a
      class="github-link"
      href="https://github.com/dasmido/nexus-pass-vault"
      target="_blank"
      rel="noreferrer"
      aria-label="Open Nexus Pass Vault on GitHub"
      title="Open Nexus Pass Vault on GitHub"
    >
      <LogoGithub />
    </a>

    <HeaderAction
      bind:isOpen={isOpen3}
      text=""
      on:open={() => {
        isOpen1 = false;
        isOpen2 = false;
      }}
    >
      <HeaderPanelLinks>
        <HeaderPanelDivider>Navigation</HeaderPanelDivider>
        <HeaderPanelLink icon={Locked} href="/passwords">Passwords</HeaderPanelLink>
        <HeaderPanelLink icon={Settings} href="/settings">Settings</HeaderPanelLink>
      </HeaderPanelLinks>
    </HeaderAction>
  </HeaderUtilities>
</Header>

<Content>
  {#if currentRoute === "/passwords"}
    <PasswordsPage />
  {:else if currentRoute === "/settings"}
    <SettingsPage />
  {/if}
</Content>

<style>
  .github-link {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 3rem;
    height: 3rem;
    color: inherit;
  }

  .github-link :global(svg) {
    width: 1.65rem;
    height: 1.65rem;
  }
</style>










