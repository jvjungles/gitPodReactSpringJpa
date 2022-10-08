import { ILocation } from 'app/shared/model/location.model';
import { IFera } from 'app/shared/model/fera.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IDepartment {
  id?: number;
  departmentName?: string;
  location?: ILocation | null;
  location?: IFera | null;
  employees?: IEmployee[] | null;
}

export const defaultValue: Readonly<IDepartment> = {};
