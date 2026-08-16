<script>
	import {
		Button,
		Column,
		Form,
		Grid,
		InlineNotification,
		Row,
		Section,
		Select,
		SelectItem,
		TextInput,
		Tile,
		Toggle,
	} from "carbon-components-svelte";
	import Password from "carbon-icons-svelte/lib/Password.svelte";
	import Security from "carbon-icons-svelte/lib/Security.svelte";
	import User from "carbon-icons-svelte/lib/User.svelte";

	let displayName = "Alex Morgan";
	let email = "alex.morgan@nexus.dev";
	let language = "en-us";
	let autoLock = "15";
	let clipboardTimeout = "30";
	let requireUnlock = true;
	let maskPasswords = true;
	let compactDensity = false;
	let saved = false;

	function saveSettings() {
		saved = true;
		window.setTimeout(() => {
			saved = false;
		}, 3500);
	}

	function handleSubmit(event) {
		event.preventDefault();
		saveSettings();
	}
</script>

<div class="settings-page">
	<Grid condensed>
		<Row>
			<Column sm={4} md={8} lg={12}>
				<div class="page-heading">
					<div>
						<p class="eyebrow">Workspace preferences</p>
						<h1>Settings</h1>
						<p class="intro">Manage your Nexus identity, vault protection, and display preferences.</p>
					</div>
					<div class="heading-mark" aria-hidden="true">N</div>
				</div>
			</Column>
		</Row>

		{#if saved}
			<Row>
				<Column sm={4} md={8} lg={12}>
					<InlineNotification kind="success" title="Settings saved" subtitle="Your preferences are now up to date." lowContrast hideCloseButton />
				</Column>
			</Row>
		{/if}

		<Row>
			<Column sm={4} md={8} lg={4}>
				<aside class="settings-index" aria-label="Settings sections">
					<p class="index-title">Settings</p>
					<a class="index-link active" href="#profile"><User size={16} /> Profile</a>
					<a class="index-link" href="#security"><Security size={16} /> Vault security</a>
					<a class="index-link" href="#preferences"><Password size={16} /> Preferences</a>
				</aside>
			</Column>

			<Column sm={4} md={8} lg={8}>
				<Form on:submit={handleSubmit}>
					<Section id="profile" class="settings-section">
						<div class="section-heading">
							<div>
								<p class="section-kicker">01 / Identity</p>
								<h2>Profile</h2>
							</div>
							<User size={24} />
						</div>
						<p class="section-copy">The details used to identify you across this vault.</p>
						<div class="field-grid">
							<TextInput id="display-name" labelText="Display name" bind:value={displayName} />
							<TextInput id="email" type="email" labelText="Email address" bind:value={email} />
						</div>
						<Button kind="tertiary" size="sm">Change profile photo</Button>
					</Section>

					<Section id="security" class="settings-section">
						<div class="section-heading">
							<div>
								<p class="section-kicker">02 / Protection</p>
								<h2>Vault security</h2>
							</div>
							<Security size={24} />
						</div>
						<p class="section-copy">Control how quickly Nexus protects an unattended vault.</p>
						<div class="security-tile">
							<div>
								<h3>Master password</h3>
								<p>Last changed 28 days ago</p>
							</div>
							<Button kind="tertiary" size="sm">Update password</Button>
						</div>
						<div class="field-grid">
							<Select id="auto-lock" labelText="Auto-lock after" bind:value={autoLock}>
								<SelectItem value="5" text="5 minutes" />
								<SelectItem value="15" text="15 minutes" />
								<SelectItem value="30" text="30 minutes" />
								<SelectItem value="never" text="Never" />
							</Select>
							<Select id="clipboard-timeout" labelText="Clear clipboard after" bind:value={clipboardTimeout}>
								<SelectItem value="15" text="15 seconds" />
								<SelectItem value="30" text="30 seconds" />
								<SelectItem value="60" text="1 minute" />
								<SelectItem value="never" text="Never" />
							</Select>
						</div>
						<div class="toggle-stack">
							<Toggle id="require-unlock" labelText="Require unlock on launch" toggled={requireUnlock} on:toggle={(event) => requireUnlock = event.detail} />
							<Toggle id="mask-passwords" labelText="Mask passwords by default" toggled={maskPasswords} on:toggle={(event) => maskPasswords = event.detail} />
						</div>
					</Section>

					<Section id="preferences" class="settings-section">
						<div class="section-heading">
							<div>
								<p class="section-kicker">03 / Workspace</p>
								<h2>Preferences</h2>
							</div>
							<Password size={24} />
						</div>
						<p class="section-copy">Tune the vault interface to fit the way you work.</p>
						<div class="field-grid">
							<Select id="language" labelText="Language" bind:value={language}>
								<SelectItem value="en-us" text="English (US)" />
								<SelectItem value="en-gb" text="English (UK)" />
								<SelectItem value="fr" text="French" />
							</Select>
						</div>
						<Tile class="preference-tile">
							<Toggle id="compact-density" labelText="Compact density" labelA="Comfortable" labelB="Compact" toggled={compactDensity} on:toggle={(event) => compactDensity = event.detail} />
							<p>Show more vault entries at once in list views.</p>
						</Tile>
					</Section>

					<div class="form-footer">
						<Button type="submit">Save changes</Button>
						<Button kind="secondary" type="button" on:click={() => window.location.reload()}>Discard</Button>
					</div>
				</Form>
			</Column>
		</Row>
	</Grid>
</div>

<style>
	:global(body) { background: #f4f4f4; }
	:global(.bx--content) { min-height: calc(100vh - 48px); }
	.settings-page { margin: 0; max-width: none; padding: 0; }
	.page-heading { display: flex; justify-content: space-between; align-items: flex-end; border-bottom: 1px solid #c6c6c6; padding: 1rem 0 2rem; margin-bottom: 2rem; }
	.eyebrow, .section-kicker, .index-title { color: #525252; font-size: .75rem; font-weight: 600; letter-spacing: .08rem; text-transform: uppercase; }
	.eyebrow { margin: 0 0 .75rem; }
	h1 { color: #161616; font-size: 2.625rem; font-weight: 400; letter-spacing: 0; line-height: 1.15; margin: 0; }
	.intro { color: #525252; font-size: 1rem; margin: .75rem 0 0; max-width: 34rem; }
	.heading-mark { align-items: center; background: #0f62fe; color: white; display: flex; font-size: 2rem; font-weight: 600; height: 4rem; justify-content: center; width: 4rem; }
	.settings-index { border-left: 2px solid #c6c6c6; margin: .25rem 2rem 0 0; padding: 0 0 1rem 1rem; position: sticky; top: 1rem; }
	.index-title { margin: 0 0 1rem; }
	.index-link { align-items: center; color: #525252; display: flex; gap: .5rem; padding: .625rem 0; text-decoration: none; }
	.index-link:hover, .index-link.active { color: #0f62fe; }
	.index-link.active { font-weight: 600; }
	:global(.settings-section) { border-top: 1px solid #c6c6c6; padding: 2rem 0 2.5rem; }
	:global(.settings-section:first-child) { border-top: 0; padding-top: 0; }
	.section-heading { align-items: flex-start; display: flex; justify-content: space-between; }
	.section-heading :global(svg) { color: #0f62fe; }
	.section-kicker { margin: 0 0 .5rem; }
	h2 { color: #161616; font-size: 1.75rem; font-weight: 400; margin: 0; }
	h3 { font-size: 1rem; font-weight: 600; margin: 0; }
	.section-copy { color: #525252; margin: .75rem 0 1.5rem; }
	.field-grid { display: grid; gap: 1rem; grid-template-columns: repeat(2, minmax(0, 1fr)); margin-bottom: 1.5rem; }
	.security-tile { align-items: center; background: #e0e0e0; display: flex; justify-content: space-between; margin-bottom: 1.5rem; padding: 1rem; }
	.security-tile p, :global(.preference-tile) p { color: #525252; font-size: .875rem; margin: .35rem 0 0; }
	.toggle-stack { border-top: 1px solid #e0e0e0; }
	.toggle-stack :global(.bx--toggle), :global(.preference-tile) :global(.bx--toggle) { border-bottom: 1px solid #e0e0e0; padding: 1rem 0; }
	:global(.preference-tile) { background: #f4f4f4; margin-top: 1rem; padding: 1rem; }
	.form-footer { border-top: 1px solid #c6c6c6; display: flex; gap: .5rem; padding-top: 1.5rem; }
	@media (max-width: 1055px) {
	}
	@media (max-width: 671px) {
		.page-heading { padding-bottom: 1.5rem; }
		h1 { font-size: 2rem; }
		.heading-mark { height: 3rem; width: 3rem; }
		.settings-index { margin: 0 0 2rem; position: static; }
		.field-grid { grid-template-columns: 1fr; }
		.security-tile { align-items: flex-start; flex-direction: column; gap: 1rem; }
	}
</style>
