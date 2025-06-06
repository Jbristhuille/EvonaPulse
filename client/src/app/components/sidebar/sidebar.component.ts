/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 15:49:03                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-06 16:26:23                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
*/

/* Imports */
import { Component, OnInit } from '@angular/core';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { IProject } from '../../interfaces/projects';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CommonModule } from '@angular/common';
/***/

/* Services */
import { ProjectService } from '../../services/data/project/project.service';
import { NzMessageService } from 'ng-zorro-antd/message';
/***/

@Component({
  selector: 'component-sidebar',
  imports: [NzMenuModule, NzSpinModule, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  public projects: IProject[] | undefined;

  constructor(private projectService: ProjectService,
              private message: NzMessageService) {
  }

  ngOnInit(): void {
    this.projectService.getAll().subscribe({
      next: (projects: IProject[]) => {
        this.projects = projects;
        console.log('Projects loaded:', this.projects);
      },
      error: (error) => {
        console.error('Error fetching projects:', error);
        this.message.error('Failed to load projects. Please try again later.');
      }
    });
  }
}
