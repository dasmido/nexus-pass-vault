import "carbon-components-svelte/css/white.css";
import { mount } from 'svelte';
import App from './App.svelte';
import './app.css';

mount(App, {
  target: document.getElementById('app')
});

