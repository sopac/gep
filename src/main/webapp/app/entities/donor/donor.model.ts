import { IProject } from 'app/entities/project/project.model';
import { IProposal } from 'app/entities/proposal/proposal.model';
import { ICountry } from 'app/entities/country/country.model';

export interface IDonor {
  id?: number;
  name?: string;
  description?: string | null;
  url?: string | null;
  city?: string | null;
  donorCategory?: string | null;
  sector?: string | null;
  keyContact?: string | null;
  projects?: IProject[] | null;
  proposals?: IProposal[] | null;
  country?: ICountry | null;
}

export class Donor implements IDonor {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public url?: string | null,
    public city?: string | null,
    public donorCategory?: string | null,
    public sector?: string | null,
    public keyContact?: string | null,
    public projects?: IProject[] | null,
    public proposals?: IProposal[] | null,
    public country?: ICountry | null
  ) {}
}

export function getDonorIdentifier(donor: IDonor): number | undefined {
  return donor.id;
}
