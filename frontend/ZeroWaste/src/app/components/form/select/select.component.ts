import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-select',
  imports: [],
  template: `
    <select
      [name]="name"
      [id]="id"
      [disabled]="disabled"
      [multiple]="multiple"
      (change)="onSelectChange($event)"
      (blur)="onTouch($event)"
    >
      @if (placeholder && !multiple) {
        <option disabled [selected]="!value">{{ placeholder }}</option>
      }
      <ng-content />
    </select>
  `,
  styleUrl: './select.component.css',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true
    }
  ]
})
export class SelectComponent implements ControlValueAccessor {
  @Input() id: HTMLSelectElement['id'] = '';
  @Input() name: HTMLSelectElement['name'] = '';
  @Input() placeholder: string = '';
  @Input() disabled: HTMLSelectElement['disabled'] = false;
  @Input() multiple: HTMLSelectElement['multiple'] = false;

  value: any = this.multiple ? [] : '';

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

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onSelectChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;

    if (this.multiple) {
      const selectedOptions = Array.from(selectElement.selectedOptions).map(
        (option: HTMLOptionElement) => option.value
      );
      this.value = selectedOptions;
      this.onChange(selectedOptions);
    } else {
      this.value = selectElement.value;
      this.onChange(selectElement.value);
    }

    this.onTouch();
  }
}
