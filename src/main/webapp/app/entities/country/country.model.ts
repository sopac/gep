import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { IDonor } from 'app/entities/donor/donor.model';
import { IContact } from 'app/entities/contact/contact.model';
import { IProject } from 'app/entities/project/project.model';
import { IProposal } from 'app/entities/proposal/proposal.model';

export interface ICountry {
  id?: number;
  name?: string;
  member?: boolean | null;
  organisations?: IOrganisation[] | null;
  donors?: IDonor[] | null;
  contacts?: IContact[] | null;
  projects?: IProject[] | null;
  proposals?: IProposal[] | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public name?: string,
    public member?: boolean | null,
    public organisations?: IOrganisation[] | null,
    public donors?: IDonor[] | null,
    public contacts?: IContact[] | null,
    public projects?: IProject[] | null,
    public proposals?: IProposal[] | null
  ) {
    this.member = this.member ?? false;
  }
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
