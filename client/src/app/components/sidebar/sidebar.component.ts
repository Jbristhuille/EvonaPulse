/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 15:49:03                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-20 14:43:03                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * Components
  * openProjectModal - Open project creation modal
*/

/* Imports */
import { Component, OnInit } from '@angular/core';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { IProject } from '../../interfaces/projects';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CommonModule } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzModalModule, NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzMessageService } from 'ng-zorro-antd/message';
/***/

/* Services */
import { ProjectService } from '../../services/data/project/project.service';
import { AuthService } from '../../services/auth/auth.service';
/***/

/* Components */
import { CreateProjectComponent } from '../modals/create-project/create-project.component';
import { RouterModule } from '@angular/router';
/***/

@Component({
  selector: 'component-sidebar',
  imports: [
    NzMenuModule,
    NzSpinModule,
    CommonModule,
    NzButtonModule,
    NzIconModule,
    NzModalModule,
    NzFormModule,
    NzInputModule,
    RouterModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  public projects: IProject[] | undefined;
  private modal: NzModalRef | undefined;

  constructor(private projectService: ProjectService,
              private message: NzMessageService,
              private authService: AuthService,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
    if (this.authService.isLogged()) {
      this.projectService.getAll().subscribe({
        next: (projects: IProject[]) => {
          this.projects = projects;
        },
        error: (error) => {
          console.error('Error fetching projects:', error);
          this.message.error('Failed to load projects. Please try again later.');
        }
      });
    }
  }

  /**
  * handleProjectCreate - Handle project creation
  */
  private handleProjectCreate(): boolean {
    if (this.modal) {
      this.modal.updateConfig({nzOkLoading: true});

      if (this.modal.getContentComponent().name.trim() === "") {
        this.modal.getContentComponent().onError = "error";
        this.message.error('Project name cannot be empty.');
        this.modal.updateConfig({nzOkLoading: false});
      } else {
        this.projectService.create(this.modal.getContentComponent().name.trim())
          .subscribe({
            next: (project: IProject) => {
              this.projects?.push(project);
              this.message.success('Project created successfully!');
              this.modal?.close();
            },
            error: (error) => {
              console.error(error);
              this.message.error(error.error?.message || 'Failed to create project. Please try again later.');
              this.modal?.updateConfig({nzOkLoading: false});
            },
            complete: () => {
              console.log('Project creation request completed.');
              this.modal?.updateConfig({nzOkLoading: false});
            }
          });
        }
    }

    return false; // Prevent default behavior to allow custom save logic
  }
  /***/

  /**
  * openProjectModal - Open project creation modal
  */
  public openProjectModal(): void {
    this.modal = this.modalService.create({
      nzTitle: 'Create New Project',
      nzContent: CreateProjectComponent,
      nzOkText: 'Save',
      nzOnOk: this.handleProjectCreate.bind(this)
    });
  }
  /***/
}
