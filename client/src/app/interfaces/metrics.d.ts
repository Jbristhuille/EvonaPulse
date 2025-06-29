import { MetricTypeEnum } from "./metric-enum";

export interface IMetric {
  id: string,
  label: string,
  name: string,
  type: MetricTypeEnum,
  logs: string[],
  createdAt: Date,
  updatedAt: Date
}
