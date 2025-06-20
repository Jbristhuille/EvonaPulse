/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-20 14:36:53                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-20 14:45:01                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CommonModule } from '@angular/common';
import { NzMessageService } from 'ng-zorro-antd/message';
/***/

/* Services */
import { ProjectService } from '../../services/data/project/project.service';
import { IProject } from '../../interfaces/projects';
/***/

@Component({
  selector: 'view-project-dashboard',
  imports: [
    NzSpinModule,
    CommonModule
  ],
  templateUrl: './project-dashboard.view.html',
  styleUrl: './project-dashboard.view.scss'
})
export class ProjectDashboardView implements OnInit {
  public project: IProject | undefined;

  constructor(private route: ActivatedRoute,
              private projectService: ProjectService,
              private message: NzMessageService) {
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
}
