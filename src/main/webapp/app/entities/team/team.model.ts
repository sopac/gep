import { IProject } from 'app/entities/project/project.model';
import { IProposal } from 'app/entities/proposal/proposal.model';

export interface ITeam {
  id?: number;
  name?: string;
  lead?: string | null;
  projects?: IProject[] | null;
  proposals?: IProposal[] | null;
}

export class Team implements ITeam {
  constructor(
    public id?: number,
    public name?: string,
    public lead?: string | null,
    public projects?: IProject[] | null,
    public proposals?: IProposal[] | null
  ) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
