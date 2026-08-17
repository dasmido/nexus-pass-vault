const SETTINGS_API = '/api/settings';

export async function getStorageStatus() {
    const response = await fetch(`${SETTINGS_API}/storage-status`);

    if (!response.ok) {
        throw new Error(`Storage settings request failed (${response.status})`);
    }

    return response.json();
}