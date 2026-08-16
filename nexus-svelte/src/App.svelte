<script>
  import { onMount } from 'svelte';

  let vaultItems = [];
  let loading = true;
  let error = '';
  let currentRoute = '/dashboard';

  function normalizeRoute(pathname) {
    if (pathname === '/' || pathname === '') {
      return '/dashboard';
    }
    return pathname;
  }

  function navigate(route) {
    if (route === currentRoute) {
      return;
    }
    window.history.pushState({}, '', route);
    currentRoute = route;
  }

  async function loadVault() {
    loading = true;
    error = '';

    try {
      const response = await fetch('/api/passwords');
      if (!response.ok) {
        throw new Error(`Failed to load passwords (${response.status})`);
      }
      vaultItems = await response.json();
    } catch (err) {
      error = err instanceof Error ? err.message : 'Unknown error';
    } finally {
      loading = false;
    }
  }

  onMount(() => {
    currentRoute = normalizeRoute(window.location.pathname);
    window.addEventListener('popstate', () => {
      currentRoute = normalizeRoute(window.location.pathname);
    });
    loadVault();
  });
</script>

<main class="container">
  <section class="hero">
    <h1>Nexus Password Vault</h1>
    <p>Unified Spring Boot + Svelte dashboard served from one host and one port.</p>
    <button on:click={loadVault}>Refresh</button>
  </section>

  <nav class="tabs">
    <button class:active={currentRoute === '/dashboard'} on:click={() => navigate('/dashboard')}>Dashboard</button>
    <button class:active={currentRoute === '/passwords'} on:click={() => navigate('/passwords')}>Passwords</button>
  </nav>

  {#if currentRoute !== '/dashboard' && currentRoute !== '/passwords'}
    <p class="status error">Route not found: {currentRoute}</p>
  {/if}

  {#if loading}
    <p class="status">Loading vault entries...</p>
  {:else if error}
    <p class="status error">{error}</p>
  {:else if currentRoute === '/dashboard'}
    <section class="card">
      <h2>Dashboard</h2>
      <div class="stats">
        <article>
          <h3>Total Entries</h3>
          <p>{vaultItems.length}</p>
        </article>
        <article>
          <h3>First Site</h3>
          <p>{vaultItems[0]?.site ?? 'N/A'}</p>
        </article>
      </div>
    </section>
  {:else if currentRoute === '/passwords'}
    <section class="card">
      <h2>Saved Passwords</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Site</th>
            <th>Username</th>
            <th>Secret</th>
          </tr>
        </thead>
        <tbody>
          {#if vaultItems.length === 0}
            <tr>
              <td colspan="4" class="empty">No entries yet</td>
            </tr>
          {:else}
            {#each vaultItems as item}
              <tr>
                <td>{item.id}</td>
                <td>{item.site}</td>
                <td>{item.username}</td>
                <td><code>{item.secret}</code></td>
              </tr>
            {/each}
          {/if}
        </tbody>
      </table>
    </section>
  {/if}
</main>


