import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectDashboardView } from './project-dashboard.view';

describe('ProjectDashboardView', () => {
  let component: ProjectDashboardView;
  let fixture: ComponentFixture<ProjectDashboardView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectDashboardView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectDashboardView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
