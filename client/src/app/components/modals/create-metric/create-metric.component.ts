/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-29 15:49:48                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-29 16:02:23                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { Component } from '@angular/core';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { MetricTypeEnum } from '../../../interfaces/metric-enum';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
/***/

@Component({
  selector: 'app-create-metric',
  imports: [
    NzInputModule,
    NzSelectModule,
    CommonModule,
    FormsModule
  ],
  templateUrl: './create-metric.component.html',
  styleUrl: './create-metric.component.scss'
})
export class CreateMetricComponent {
  public metricTypes: MetricTypeEnum[] = Object.values(MetricTypeEnum) as MetricTypeEnum[];
  public label: string = '';
  public type: MetricTypeEnum = MetricTypeEnum.STRING;
  public name: string = '';
}
