<script>
  import { onMount } from 'svelte';

  let vaultItems = [];
  let loading = true;
  let error = '';
  let saving = false;
  let formError = '';
  let currentRoute = '/dashboard';
  let form = {
    id: null,
    site: '',
    username: '',
    secret: ''
  };

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

  function resetForm() {
    form = {
      id: null,
      site: '',
      username: '',
      secret: ''
    };
    formError = '';
  }

  function startEdit(item) {
    form = {
      id: item.id,
      site: item.site,
      username: item.username,
      secret: item.secret
    };
    formError = '';
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

  async function submitForm() {
    if (!form.site || !form.username || !form.secret) {
      formError = 'All fields are required.';
      return;
    }

    saving = true;
    formError = '';

    try {
      const isUpdate = form.id !== null;
      const url = isUpdate ? `/api/passwords/${form.id}` : '/api/passwords';
      const method = isUpdate ? 'PUT' : 'POST';

      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          site: form.site,
          username: form.username,
          secret: form.secret
        })
      });

      if (!response.ok) {
        throw new Error(`${isUpdate ? 'Update' : 'Create'} failed (${response.status})`);
      }

      resetForm();
      await loadVault();
    } catch (err) {
      formError = err instanceof Error ? err.message : 'Unknown error';
    } finally {
      saving = false;
    }
  }

  async function deleteEntry(id) {
    if (!window.confirm('Delete this password entry?')) {
      return;
    }

    try {
      const response = await fetch(`/api/passwords/${id}`, {
        method: 'DELETE'
      });

      if (!response.ok) {
        throw new Error(`Delete failed (${response.status})`);
      }

      if (form.id === id) {
        resetForm();
      }

      await loadVault();
    } catch (err) {
      formError = err instanceof Error ? err.message : 'Unknown error';
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
      <div class="editor">
        <h3>{form.id === null ? 'Create Password' : `Edit #${form.id}`}</h3>
        <div class="form-grid">
          <input placeholder="Site" bind:value={form.site} />
          <input placeholder="Username" bind:value={form.username} />
          <input placeholder="Secret" bind:value={form.secret} />
        </div>
        <div class="form-actions">
          <button on:click={submitForm} disabled={saving}>{form.id === null ? 'Create' : 'Update'}</button>
          <button class="muted" on:click={resetForm} disabled={saving}>Clear</button>
        </div>
        {#if formError}
          <p class="status error">{formError}</p>
        {/if}
      </div>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Site</th>
            <th>Username</th>
            <th>Secret</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {#if vaultItems.length === 0}
            <tr>
              <td colspan="5" class="empty">No entries yet</td>
            </tr>
          {:else}
            {#each vaultItems as item}
              <tr>
                <td>{item.id}</td>
                <td>{item.site}</td>
                <td>{item.username}</td>
                <td><code>{item.secret}</code></td>
                <td class="actions">
                  <button class="small" on:click={() => startEdit(item)}>Edit</button>
                  <button class="small danger" on:click={() => deleteEntry(item.id)}>Delete</button>
                </td>
              </tr>
            {/each}
          {/if}
        </tbody>
      </table>
    </section>
  {/if}
</main>


