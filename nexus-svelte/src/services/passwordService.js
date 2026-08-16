const PASSWORDS_API = "/api/passwords";

async function request(url, options) {
  const response = await fetch(url, options);

  if (!response.ok) {
    throw new Error(`Password request failed (${response.status})`);
  }

  return response.status === 204 ? undefined : response.json();
}

export function getPasswords() {
  return request(PASSWORDS_API);
}

export function getPassword(id) {
  return request(`${PASSWORDS_API}/${id}`);
}

export function createPassword(password) {
  return request(PASSWORDS_API, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(password),
  });
}

export function updatePassword(id, password) {
  return request(`${PASSWORDS_API}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(password),
  });
}

export function deletePassword(id) {
  return request(`${PASSWORDS_API}/${id}`, { method: "DELETE" });
}