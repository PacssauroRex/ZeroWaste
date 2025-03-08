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
      (input)="onInputChange($event)"
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
  @Input() id: HTMLInputElement['id'] = '';
  @Input() name: HTMLInputElement['name'] = '';
  @Input() type: HTMLInputElement['type'] = 'text';
  @Input() placeholder: HTMLInputElement['placeholder'] = '';
  @Input() value: HTMLInputElement['value'] = '';

  onChange: any = () => {};
  onTouch: any = () => {};

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  onInputChange(event: Event): void {
    this.writeValue((event.target as HTMLInputElement).value);
    this.onChange((event.target as HTMLInputElement).value);
  }
}
