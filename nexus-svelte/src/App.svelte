<script>
  import { onMount } from "svelte";
  import {
    Column,
    Content,
    Grid,
    Header,
    HeaderNav,
    HeaderNavItem,
    HeaderNavMenu,
    Row,
    SideNav,
    SideNavDivider,
    SideNavItems,
    SideNavLink,
    SideNavMenu,
    SideNavMenuItem,
    SkipToContent,
  } from "carbon-components-svelte";
  import Locked from "carbon-icons-svelte/lib/Locked.svelte";
  import Dashboard from "carbon-icons-svelte/lib/Dashboard.svelte";
  import Kubernetes from "carbon-icons-svelte/lib/Kubernetes.svelte";
  import ListBoxes from "carbon-icons-svelte/lib/ListBoxes.svelte";
  import Settings from "carbon-icons-svelte/lib/Settings.svelte";
  import PasswordsPage from "./pages/PasswordsPage.svelte";
  import SettingsPage from "./pages/SettingsPage.svelte";

  let isSideNavOpen = true;
  let currentRoute = "/dashboard";

  onMount(() => {
    currentRoute = window.location.pathname;
    
    // Redirect root path to /passwords
    if (currentRoute === "/") {
      window.location.href = "/passwords";
    }
  });
</script>

<Header companyName="Nexus Pass Vault" bind:isSideNavOpen>
  <svelte:fragment slot="skipToContent"><SkipToContent /></svelte:fragment>
  <!--<HeaderNav>
    <HeaderNavItem href="/catalog" text="Catalog" />
    <HeaderNavItem href="/docs" text="Docs" />
    <HeaderNavItem href="/support" text="Support" />
    <HeaderNavMenu text="Manage">
      <HeaderNavItem href="/account" text="Account" />
      <HeaderNavItem href="/iam" text="Access (IAM)" />
      <HeaderNavItem href="/billing" text="Billing and usage" />
    </HeaderNavMenu>
    <HeaderNavItem href="/status" text="Status" />
  </HeaderNav>-->
</Header>

<SideNav bind:isOpen={isSideNavOpen}>
  <SideNavItems>

    <SideNavLink icon={Locked} text="Passwords" href="/passwords" isSelected={currentRoute === "/passwords"} />
    
    <!--<SideNavDivider />-->
    <SideNavLink icon={Settings} text="Settings" href="/settings" isSelected={currentRoute === "/settings"} />
  </SideNavItems>
</SideNav>

<Content>
  {#if currentRoute === "/passwords"}
    <PasswordsPage />
  {:else if currentRoute === "/settings"}
    <SettingsPage />
  {/if}
</Content>

<style>
  :global(.bx--content) {
    margin: 0;
    padding: 0;
  }
</style>
