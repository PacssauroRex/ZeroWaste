import { Component, NgModule } from '@angular/core';

@Component({
  selector: 'app-card-header',
  template: `
    <header class="card-header">
      <ng-content />
    </header>
  `,
  styleUrl: './card.component.css',
  standalone: true,
})
export class CardHeaderComponent {}

@Component({
  selector: 'app-card-content',
  template: `
    <main class="card-content">
      <ng-content />
    </main>
  `,
  styleUrl: './card.component.css',
  standalone: true,
})
export class CardContentComponent {}

@Component({
  selector: 'app-card-footer',
  template: `
    <footer class="card-footer">
      <ng-content />
    </footer>
  `,
  styleUrl: './card.component.css',
  standalone: true,
})
export class CardFooterComponent {}

@Component({
  selector: 'app-card',
  template: `
  <article #card class="card">
    <ng-content />
  </article>`,
  styleUrl: './card.component.css',
  standalone: true,
})
export class CardComponent {}
