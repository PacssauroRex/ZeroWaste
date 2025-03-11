import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';

@Component({
  selector: 'app-multi-select',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './multi-select.component.html',
  styleUrl: './multi-select.component.css'
})
export class MultiSelectComponent implements OnInit {

  @Input() id = '';
  @Input() options: any[] = [];
  @Input() labelKey: string = '';
  @Input() valueKey: string = '';
  @Input() preselectedItems: any[] = [];
  @Output() selectionChange = new EventEmitter<any[]>();

  selectedItems: any[] = [];

  ngOnInit() {
    this.selectedItems = this.options.filter(option =>
      this.preselectedItems.some(preselected => preselected[this.valueKey] === option[this.valueKey])
    );

    this.emitSelection();
  }

  toggleSelection(option: any) {
    const index = this.selectedItems.findIndex(item => item[this.valueKey] === option[this.valueKey]);

    if (index === -1) {
      this.selectedItems = [...this.selectedItems, option];
    } else {
      this.selectedItems = this.selectedItems.filter(item => item[this.valueKey] !== option[this.valueKey]);
    }

    this.emitSelection();
  }

  isSelected(option: any): boolean {
    return this.selectedItems.some(item => item[this.valueKey] === option[this.valueKey]);
  }

  private emitSelection(): void {
    this.selectionChange.emit([...this.selectedItems]);
  }

  trackByFn(index: number, item: any): any {
    return item[this.valueKey];
  }
}
