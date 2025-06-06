/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 16:10:50                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-06 16:26:20                              *
 ****************************************************************************/

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SidebarComponent } from './sidebar.component';
import { ProjectService } from '../../services/data/project/project.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { of, throwError } from 'rxjs';
import { IProject } from '../../interfaces/projects';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { CommonModule } from '@angular/common';
import { By } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { ProjectOutline } from '@ant-design/icons-angular/icons';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;
  let projectService: jasmine.SpyObj<ProjectService>;
  let messageService: jasmine.SpyObj<NzMessageService>;

  const mockProjects: IProject[] = [
    {
      id: '1',
      name: 'Test Project 1',
      apiKey: 'key1',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: '2',
      name: 'Test Project 2',
      apiKey: 'key2',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(async () => {
    const projectSpy = jasmine.createSpyObj('ProjectService', ['getAll']);
    const messageSpy = jasmine.createSpyObj('NzMessageService', ['error']);

    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        NzSpinModule,
        NzMenuModule,
        SidebarComponent,
        NzIconModule.forRoot([ProjectOutline])
      ],
      declarations: [],
      providers: [
        { provide: ProjectService, useValue: projectSpy },
        { provide: NzMessageService, useValue: messageSpy },
        provideAnimations()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    projectService = TestBed.inject(ProjectService) as jasmine.SpyObj<ProjectService>;
    messageService = TestBed.inject(NzMessageService) as jasmine.SpyObj<NzMessageService>;
  });

it('should load and display projects', async () => {
  projectService.getAll.and.returnValue(of(mockProjects));

  fixture.detectChanges();
  await fixture.whenStable();

  const items = fixture.debugElement.queryAll(By.css('li[nz-menu-item]'));
  expect(items.length).toBe(2);
  expect(items[0].nativeElement.textContent).toContain('Test Project 1');
  expect(items[1].nativeElement.textContent).toContain('Test Project 2');
});


  it('should show loader if projects are undefined', () => {
    projectService.getAll.and.returnValue(of(undefined as unknown as IProject[]));

    fixture.detectChanges();

    const loader = fixture.debugElement.query(By.css('nz-spin'));
    expect(loader).toBeTruthy();
  });

  it('should show error message if project load fails', () => {
    projectService.getAll.and.returnValue(throwError(() => ({ status: 500 })));

    fixture.detectChanges();

    expect(messageService.error).toHaveBeenCalledWith('Failed to load projects. Please try again later.');
  });
});
