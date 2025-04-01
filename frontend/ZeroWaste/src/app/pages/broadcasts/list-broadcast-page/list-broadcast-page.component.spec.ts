import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListBroadcastPageComponent } from './list-broadcast-page.component';

describe('ListBroadcastPageComponent', () => {
  let component: ListBroadcastPageComponent;
  let fixture: ComponentFixture<ListBroadcastPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListBroadcastPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListBroadcastPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
