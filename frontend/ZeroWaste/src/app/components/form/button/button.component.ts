import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  imports: [],
  template: `
    <button
      [classList]="[variant, block ? 'block' : ''].join(' ').trim()"
      [type]="type"
      [disabled]="disabled"
    >
      <ng-content />
    </button>
  `,
  styleUrl: './button.component.css',
  standalone: true,
})
export class ButtonComponent {
  @Input() type: HTMLButtonElement['type'] = 'button';
  @Input() disabled: HTMLButtonElement['disabled'] = false;
  @Input() variant: 'default' | 'destructive' | 'outline' | 'subtle' = 'default';
  @Input() block: boolean = false;

  @Output() click = new EventEmitter<MouseEvent>();

  constructor() {}
}
