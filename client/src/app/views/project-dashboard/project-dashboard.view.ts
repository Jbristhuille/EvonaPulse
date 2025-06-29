/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-20 14:36:53                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-29 13:06:25                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * loadMetrics - Load metrics for the project
  * loadProject - Load project details by ID
  * handleProjectDelete - Handle project deletion
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
import { NzPopoverModule } from 'ng-zorro-antd/popover';
/***/

/* Services */
import { ProjectService } from '../../services/data/project/project.service';
import { IProject } from '../../interfaces/projects';
import { MetricService } from '../../services/data/metric/metric.service';
import { IMetric } from '../../interfaces/metrics';
/***/

@Component({
  selector: 'view-project-dashboard',
  imports: [
    NzSpinModule,
    CommonModule,
    NzButtonModule,
    NzIconModule,
    NzModalModule,
    NzPopoverModule
  ],
  templateUrl: './project-dashboard.view.html',
  styleUrl: './project-dashboard.view.scss'
})
export class ProjectDashboardView implements OnInit {
  public project: IProject | undefined;
  public metrics: IMetric[] | undefined;
  public modal: NzModalRef | undefined;

  constructor(private route: ActivatedRoute,
              private projectService: ProjectService,
              private message: NzMessageService,
              private modalService: NzModalService,
              private metricService: MetricService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.loadProject(params.get('id') || '')
        .then(() => {
          this.loadMetrics(this.project?.id || '')
        });
    });
  }

  /**
  * loadMetrics - Load metrics for the project
  * @param id - The ID of the project to load metrics for
  */
  private loadMetrics(id: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.metricService.getMetrics(id).subscribe({
        next: (metrics) => {
          this.metrics = metrics;
          return resolve();
        },
        error: (error) => {
          console.error('Error fetching metrics:', error);
          this.message.error('Failed to load project metrics. Please try again later.');
          return reject(error);
        }
      });
    });
  }
  /***/

  /**
  * loadProject - Load project details by ID
  * @param id - The ID of the project to load
  */
  private loadProject(id: string): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.projectService.getById(id).subscribe({
        next: (project: IProject) => {
          this.project = project;
          return resolve();
        },
        error: (error) => {
          console.error('Error fetching project:', error);
          this.message.error('Failed to load project details. Please try again later.');
          return reject(error);
        }
      });
    });
  }
  /***/

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
      nzTitle: 'Confirm Project Deletion',
      nzContent: "Are you sure you want to remove this project? This action cannot be undone.",
      nzOkText: 'Delete',
      nzOnOk: this.handleProjectDelete.bind(this)
    });
  }
  /***/
}
