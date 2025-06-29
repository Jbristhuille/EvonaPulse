import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMetricComponent } from './create-metric.component';

describe('CreateMetricComponent', () => {
  let component: CreateMetricComponent;
  let fixture: ComponentFixture<CreateMetricComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateMetricComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateMetricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
