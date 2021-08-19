import { IResource } from 'app/entities/resource/resource.model';
import { ICountry } from 'app/entities/country/country.model';
import { ITeam } from 'app/entities/team/team.model';
import { IDonor } from 'app/entities/donor/donor.model';
import { Status } from 'app/entities/enumerations/status.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IProposal {
  id?: number;
  title?: string;
  description?: string | null;
  status?: Status | null;
  divsionEndorsement?: boolean | null;
  pacificCommunityEndorsement?: boolean | null;
  totalBudgetCurrency?: Currency | null;
  totalBudget?: number | null;
  totalBudgetBreakdown?: string | null;
  keyThematicAreas?: string | null;
  lessonsLearntBestPractices?: string | null;
  fullCostRecoveryCoverage?: string | null;
  notes?: string | null;
  resources?: IResource[] | null;
  countries?: ICountry[] | null;
  team?: ITeam | null;
  donor?: IDonor | null;
}

export class Proposal implements IProposal {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public status?: Status | null,
    public divsionEndorsement?: boolean | null,
    public pacificCommunityEndorsement?: boolean | null,
    public totalBudgetCurrency?: Currency | null,
    public totalBudget?: number | null,
    public totalBudgetBreakdown?: string | null,
    public keyThematicAreas?: string | null,
    public lessonsLearntBestPractices?: string | null,
    public fullCostRecoveryCoverage?: string | null,
    public notes?: string | null,
    public resources?: IResource[] | null,
    public countries?: ICountry[] | null,
    public team?: ITeam | null,
    public donor?: IDonor | null
  ) {
    this.divsionEndorsement = this.divsionEndorsement ?? false;
    this.pacificCommunityEndorsement = this.pacificCommunityEndorsement ?? false;
  }
}

export function getProposalIdentifier(proposal: IProposal): number | undefined {
  return proposal.id;
}
