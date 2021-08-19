import { IContact } from 'app/entities/contact/contact.model';
import { ICountry } from 'app/entities/country/country.model';
import { OrganisationCategory } from 'app/entities/enumerations/organisation-category.model';

export interface IOrganisation {
  id?: number;
  name?: string;
  acronym?: string | null;
  organisationCategory?: OrganisationCategory | null;
  url?: string | null;
  contacts?: IContact[] | null;
  country?: ICountry | null;
}

export class Organisation implements IOrganisation {
  constructor(
    public id?: number,
    public name?: string,
    public acronym?: string | null,
    public organisationCategory?: OrganisationCategory | null,
    public url?: string | null,
    public contacts?: IContact[] | null,
    public country?: ICountry | null
  ) {}
}

export function getOrganisationIdentifier(organisation: IOrganisation): number | undefined {
  return organisation.id;
}
