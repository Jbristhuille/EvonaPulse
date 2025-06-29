/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-20 14:36:53                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-20 16:10:01                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * openProjectRemoveModal - Manage project removal modal
*/

/* Imports */
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CommonModule } from '@angular/common';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzModalModule, NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
/***/

/* Services */
import { ProjectService } from '../../services/data/project/project.service';
import { IProject } from '../../interfaces/projects';
/***/

@Component({
  selector: 'view-project-dashboard',
  imports: [
    NzSpinModule,
    CommonModule,
    NzButtonModule,
    NzIconModule,
    NzModalModule
  ],
  templateUrl: './project-dashboard.view.html',
  styleUrl: './project-dashboard.view.scss'
})
export class ProjectDashboardView implements OnInit {
  public project: IProject | undefined;
  public modal: NzModalRef | undefined;

  constructor(private route: ActivatedRoute,
              private projectService: ProjectService,
              private message: NzMessageService,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.projectService.getById(params.get('id') || '').subscribe({
        next: (project: IProject) => {
          this.project = project;
        },
        error: (error) => {
          console.error('Error fetching project:', error);
          this.message.error('Failed to load project details. Please try again later.');
        }
      });
    });
  }

  /**
  * handleProjectDelete - Handle project deletion
  * @returns boolean - Returns true if the project was successfully deleted, false otherwise
  */
  private handleProjectDelete(): boolean {
    if (this.project) {
      this.modal?.updateConfig({nzOkLoading: true});
      this.projectService.delete(this.project.id).subscribe({
        next: () => {
          this.message.success('Project deleted successfully.');
          this.modal?.updateConfig({nzOkLoading: true});
          this.modal?.close();
          document.location.href = '/dashboard'; // Redirect dashboard and force reload - tmp
        },
        error: (error) => {
          console.error('Error deleting project:', error);
          this.message.error('Failed to delete project. Please try again later.');
          this.modal?.updateConfig({nzOkLoading: false});
        }
      });
    }
    return false; // Prevent default modal behavior
  }
  /***/

  /**
  * openProjectRemoveModal - Manage project removal modal
  */
  public openProjectRemoveModal(): void {
    this.modal = this.modalService.create({
      nzTitle: 'Create New Project',
      nzContent: "Are you sure you want to remove this project? This action cannot be undone.",
      nzOkText: 'Save',
      nzOnOk: this.handleProjectDelete.bind(this)
    });
  }
  /***/
}
