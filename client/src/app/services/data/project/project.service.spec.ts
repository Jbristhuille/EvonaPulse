/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 15:45:07                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-06 15:46:20                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { ProjectService } from './project.service';
import { AuthService } from '../../auth/auth.service';
import { IProject } from '../../../interfaces/projects';
import { environment as env } from '../../../../environments/environment';
/***/

describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;
  let mockAuthService: Partial<AuthService>;

  beforeEach(() => {
    mockAuthService = {
      getToken: () => 'mocked-token'
    };

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ProjectService,
        { provide: AuthService, useValue: mockAuthService }
      ]
    });

    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all projects (getAll)', () => {
    const mockProjects: IProject[] = [
      {
        id: '1',
        name: 'Project A',
        apiKey: 'KEY1',
        createdAt: new Date('2025-01-01T00:00:00Z'),
        updatedAt: new Date('2025-01-02T00:00:00Z')
      },
      {
        id: '2',
        name: 'Project B',
        apiKey: 'KEY2',
        createdAt: new Date('2025-02-01T00:00:00Z'),
        updatedAt: new Date('2025-02-02T00:00:00Z')
      }
    ];

    service.getAll().subscribe(projects => {
      expect(projects.length).toBe(2);
      expect(projects).toEqual(mockProjects);
    });

    const req = httpMock.expectOne(`${env.API_URL}/api/projects`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer mocked-token');

    req.flush(mockProjects);
  });


  it('should retrieve project by ID (success)', () => {
    const mockProject: IProject = {
      id: '123',
      name: 'Test Project',
      apiKey: 'ABC123',
      createdAt: new Date('2025-01-01T00:00:00Z'),
      updatedAt: new Date('2025-01-02T00:00:00Z')
    };

    service.getById('123').subscribe(project => {
      expect(project).toEqual(mockProject);
    });

    const req = httpMock.expectOne(`${env.API_URL}/api/projects/123`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer mocked-token');

    req.flush(mockProject);
  });

  it('should return error if project ID not found (404)', () => {
    service.getById('bad-id').subscribe({
      next: () => fail('Expected error'),
      error: (error) => {
        expect(error.status).toBe(404);
      }
    });

    const req = httpMock.expectOne(`${env.API_URL}/api/projectsbad-id`);
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });
});
