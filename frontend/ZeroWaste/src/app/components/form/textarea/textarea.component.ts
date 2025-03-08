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
      (blur)="onTouch($event)"
      (input)="onTextareaChange($event)"
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
  @Input() placeholder: HTMLInputElement['placeholder'] = '';
  @Input() value: HTMLInputElement['value'] = '';

  onChange: any = () => {};
  onTouch: any = () => {};

  writeValue(obj: any): void {
    this.value = obj;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

  onTextareaChange(event: Event): void {
    this.writeValue((event.target as HTMLInputElement).value);
    this.onChange((event.target as HTMLInputElement).value);
  }
}
