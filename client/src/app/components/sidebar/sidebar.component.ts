/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 15:49:03                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-10 17:02:18                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * Components
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
    NzInputModule
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
  * openProjectModal - Open project creation modal
  */
  public openProjectModal(): void {
    this.modal = this.modalService.create({
      nzTitle: 'Create New Project',
      nzContent: CreateProjectComponent,
      nzOkText: 'Save',
      nzOnOk: () => {
        this.modal?.updateConfig({nzOkLoading: true}); // Start loading state
        console.log(this.modal?.getContentComponent());
        return false; // Prevent default behavior to allow custom save logic
      }
    });
  }
  /***/
}
