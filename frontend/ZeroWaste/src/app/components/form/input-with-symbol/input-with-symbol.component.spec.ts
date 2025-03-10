import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputWithSymbolComponent } from './input-with-symbol.component';

describe('InputWithSymbolComponent', () => {
  let component: InputWithSymbolComponent;
  let fixture: ComponentFixture<InputWithSymbolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InputWithSymbolComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputWithSymbolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
