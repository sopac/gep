import * as dayjs from 'dayjs';
import { IResource } from 'app/entities/resource/resource.model';
import { ICountry } from 'app/entities/country/country.model';
import { ITeam } from 'app/entities/team/team.model';
import { IDonor } from 'app/entities/donor/donor.model';
import { IContact } from 'app/entities/contact/contact.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IProject {
  id?: number;
  title?: string;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: Status | null;
  totalBudgetCurrency?: Currency | null;
  totalBudget?: number | null;
  totalBudgetBreakdown?: string | null;
  sustainableDevelopmentGoal?: string | null;
  frameworkResilientDevelopmentPacific?: string | null;
  fullCostRecoveryCoverage?: string | null;
  resources?: IResource[] | null;
  countries?: ICountry[] | null;
  team?: ITeam | null;
  donor?: IDonor | null;
  focalContact?: IContact | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public title?: string,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public status?: Status | null,
    public totalBudgetCurrency?: Currency | null,
    public totalBudget?: number | null,
    public totalBudgetBreakdown?: string | null,
    public sustainableDevelopmentGoal?: string | null,
    public frameworkResilientDevelopmentPacific?: string | null,
    public fullCostRecoveryCoverage?: string | null,
    public resources?: IResource[] | null,
    public countries?: ICountry[] | null,
    public team?: ITeam | null,
    public donor?: IDonor | null,
    public focalContact?: IContact | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
