<script>
  import { onMount } from "svelte";
  import { Button, Column, Grid, InlineNotification, Row, Tile } from "carbon-components-svelte";
  import { getStorageStatus } from "../services/settingsService.js";

  let storageStatus;
  let loading = true;
  let error = "";

  async function loadStorageStatus() {
    loading = true;
    error = "";

    try {
      storageStatus = await getStorageStatus();
    } catch (requestError) {
      storageStatus = undefined;
      error = requestError instanceof Error
        ? requestError.message
        : "Unable to retrieve storage status.";
    } finally {
      loading = false;
    }
  }

  onMount(loadStorageStatus);
</script>

<div class="settings-page">
  <Grid condensed>
    <Row>
      <Column sm={4} md={8} lg={12}>
        <div class="page-heading">
          <div>
            <p class="eyebrow">System configuration</p>
            <h1>Settings</h1>
          </div>
        </div>
      </Column>
    </Row>

    <Row>
      <Column sm={4} md={8} lg={4}>
   
      </Column>

      <Column sm={4} md={8} lg={8}>
        <section id="storage" class="settings-section">
          <div class="section-heading">
            <div>
              <p class="section-kicker">01 / Infrastructure</p>
              <h2>Database</h2>
            </div>

            {#if !loading && storageStatus}
              <span class:connected={storageStatus.connected} class="status-badge">
                {storageStatus.connected ? "Connected" : "Unavailable"}
              </span>
            {/if}
          </div>

          <p class="section-copy">
            Connection details are supplied by the server environment and cannot be changed from the vault.
          </p>

          {#if error}
            <InlineNotification
              kind="error"
              title="Storage status unavailable"
              subtitle={error}
              lowContrast
              hideCloseButton
            />
          {:else if loading}
            <Tile class="storage-tile">
              <p class="loading-copy">Checking the database connection...</p>
            </Tile>
          {:else}
            <Tile class="storage-tile">
              <dl class="storage-details">
                <div>
                  <dt>Provider</dt>
                  <dd>{storageStatus.provider ?? "PostgreSQL"}</dd>
                </div>
                <div>
                  <dt>Database</dt>
                  <dd>{storageStatus.database ?? "Not available"}</dd>
                </div>
                <div>
                  <dt>Host</dt>
                  <dd>{storageStatus.host ?? "Not available"}</dd>
                </div>
                <div>
                  <dt>Port</dt>
                  <dd>{storageStatus.port ?? "Not available"}</dd>
                </div>
              </dl>
            </Tile>
          {/if}

          <div class="section-actions">
            <Button kind="secondary" size="sm" on:click={loadStorageStatus} disabled={loading}>
              Refresh status
            </Button>
          </div>
        </section>
      </Column>
    </Row>
  </Grid>
</div>

<style>
	.settings-section { border-top: 1px solid #c6c6c6; padding: 0 0 2.5rem; }
.section-heading { align-items: flex-start; display: flex; justify-content: space-between; }
.section-kicker, .eyebrow, .index-title {
  color: #525252;
  font-size: .75rem;
  font-weight: 600;
  letter-spacing: .08rem;
  text-transform: uppercase;
}
.section-copy { color: #525252; margin: .75rem 0 1.5rem; max-width: 40rem; }
.storage-tile { background: #e8e8e8; padding: 0; }
.storage-details { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); margin: 0; }
.storage-details div { border-bottom: 1px solid #c6c6c6; padding: 1rem; }
.storage-details dt { color: #525252; font-size: .75rem; margin-bottom: .35rem; }
.storage-details dd { color: #161616; font-size: 1rem; margin: 0; }
.status-badge { background: #da1e28; color: white; font-size: .75rem; font-weight: 600; padding: .35rem .5rem; }
.status-badge.connected { background: #198038; }
.section-actions { margin-top: 1.5rem; }
.loading-copy { color: #525252; margin: 0; padding: 1rem; }

@media (max-width: 671px) {
  .storage-details { grid-template-columns: 1fr; }
}
</style>