import { Component, Input, forwardRef, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-input',
  imports: [],
  template: `
    <input
      [id]="id"
      [name]="name"
      [type]="type"
      [placeholder]="placeholder"
      (blur)="onTouch($event)"
      (input)="onChange($event)"
    />
  `,
  styleUrl: './input.component.css',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true
    }
  ],
  host: {
    '[value]': 'value',
  },
})
export class InputComponent implements ControlValueAccessor {
  // Input
  @Input() id: HTMLInputElement['id'] = '';
  @Input() name: HTMLInputElement['name'] = '';
  @Input() type: HTMLInputElement['type'] = 'text';
  @Input() placeholder: HTMLInputElement['placeholder'] = '';
  @Input() value: HTMLInputElement['value'] = '';

  // ControlValueAccessor
  onChange: any = (event: any) => {

  };
  onTouch: any = () => {};

  constructor () {}

  writeValue(value: any): void {
    this.value = value;
    this.onChange(this.value);
    this.onTouch(this.value);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }
}
