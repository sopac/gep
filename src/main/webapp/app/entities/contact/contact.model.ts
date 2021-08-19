import { IProject } from 'app/entities/project/project.model';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { ICountry } from 'app/entities/country/country.model';
import { Category } from 'app/entities/enumerations/category.model';
import { Field } from 'app/entities/enumerations/field.model';
import { Sector } from 'app/entities/enumerations/sector.model';

export interface IContact {
  id?: number;
  active?: boolean | null;
  category?: Category | null;
  photoContentType?: string | null;
  photo?: string | null;
  name?: string;
  staff?: boolean | null;
  designation?: string | null;
  division?: string | null;
  field?: Field | null;
  sector?: Sector | null;
  email?: string | null;
  phone?: string | null;
  city?: string | null;
  linkedin?: string | null;
  twitter?: string | null;
  facebook?: string | null;
  skype?: string | null;
  membershipAffiliation?: string | null;
  notes?: string | null;
  projects?: IProject[] | null;
  organisation?: IOrganisation | null;
  country?: ICountry | null;
}

export class Contact implements IContact {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public category?: Category | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public name?: string,
    public staff?: boolean | null,
    public designation?: string | null,
    public division?: string | null,
    public field?: Field | null,
    public sector?: Sector | null,
    public email?: string | null,
    public phone?: string | null,
    public city?: string | null,
    public linkedin?: string | null,
    public twitter?: string | null,
    public facebook?: string | null,
    public skype?: string | null,
    public membershipAffiliation?: string | null,
    public notes?: string | null,
    public projects?: IProject[] | null,
    public organisation?: IOrganisation | null,
    public country?: ICountry | null
  ) {
    this.active = this.active ?? false;
    this.staff = this.staff ?? false;
  }
}

export function getContactIdentifier(contact: IContact): number | undefined {
  return contact.id;
}
