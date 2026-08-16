import { defineConfig } from 'vite';
import { svelte, vitePreprocess } from '@sveltejs/vite-plugin-svelte';
import { optimizeCss, optimizeImports } from "carbon-preprocess-svelte";

export default defineConfig({
  plugins: [
  svelte({
        preprocess: [vitePreprocess(), optimizeImports()],
      }),
      optimizeCss()
      ],
  build: {
    outDir: 'dist',
    emptyOutDir: true
  }
});

