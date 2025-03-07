import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-textarea',
  imports: [],
  template: `
    <textarea
      [id]="id"
      [name]="name"
      [placeholder]="placeholder"
      [value]="value"
      (input)="onChange($event)"
      (blur)="onTouch($event)"
    >{{ value }}</textarea>
  `,
  styleUrl: './textarea.component.css',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TextareaComponent),
      multi: true,
    },
  ],
  host: {
    '[value]': 'value'
  },
})
export class TextareaComponent implements ControlValueAccessor {
  @Input() id: HTMLInputElement['id'] = '';
  @Input() name: HTMLInputElement['name'] = '';
  @Input() type: HTMLInputElement['type'] = 'text';
  @Input() placeholder: HTMLInputElement['placeholder'] = '';
  @Input() value: HTMLInputElement['value'] = '';

  onChange: any = () => {};
  onTouch: any = () => {};

  constructor () {}

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  writeValue(obj: any): void {
    console.log('obj', obj);

    this.value = obj;
    this.onChange(this.value);
    this.onTouch(this.value);
  }
}
