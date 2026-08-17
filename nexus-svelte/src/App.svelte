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
    
    <HeaderAction
      bind:isOpen={isOpen2}
      iconDescription="Help"
      icon={LogoGithub}
      on:open={() => {
        isOpen1 = false;
        isOpen3 = false;
      }}
    >
      <HeaderPanelLinks>
        <HeaderPanelDivider>Support</HeaderPanelDivider>
        <HeaderPanelLink href="/docs">Documentation</HeaderPanelLink>
      </HeaderPanelLinks>
    </HeaderAction>
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










