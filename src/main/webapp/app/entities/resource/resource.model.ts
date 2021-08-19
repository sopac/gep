import { IProject } from 'app/entities/project/project.model';
import { IProposal } from 'app/entities/proposal/proposal.model';

export interface IResource {
  id?: number;
  name?: string;
  url?: string | null;
  fileContentType?: string | null;
  file?: string | null;
  projects?: IProject[] | null;
  proposals?: IProposal[] | null;
}

export class Resource implements IResource {
  constructor(
    public id?: number,
    public name?: string,
    public url?: string | null,
    public fileContentType?: string | null,
    public file?: string | null,
    public projects?: IProject[] | null,
    public proposals?: IProposal[] | null
  ) {}
}

export function getResourceIdentifier(resource: IResource): number | undefined {
  return resource.id;
}
