import { Component, Input, forwardRef, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-input-with-symbol',
  imports: [],
  template: `
<div class="input-container">
  <input
    [id]="id"
    [name]="name"
    [type]="type"
    [placeholder]="placeholder"
    [value]="value"
    [disabled]="disabled"
    (blur)="onTouch($event)"
    (input)="onInputChange($event)"
  />
  <span class="input-icon">{{symbol}}</span>
</div>

  `,
  styleUrl: './input-with-symbol.component.css',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputWithSymbolComponent),
      multi: true
    }
  ],
  host: {
    '[value]': 'value',
  },
})
export class InputWithSymbolComponent implements ControlValueAccessor {
  @Input() id: HTMLInputElement['id'] = '';
  @Input() name: HTMLInputElement['name'] = '';
  @Input() type: HTMLInputElement['type'] = 'text';
  @Input() placeholder: HTMLInputElement['placeholder'] = '';
  @Input() value: HTMLInputElement['value'] = '';
  @Input() disabled: HTMLInputElement['disabled'] = false;
  @Input() symbol: string = '';

  onChange: any = () => { };
  onTouch: any = () => { };

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

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
