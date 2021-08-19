export interface ITool {
  id?: number;
  title?: string | null;
  description?: string | null;
  url?: string | null;
  fileContentType?: string | null;
  file?: string | null;
}

export class Tool implements ITool {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public url?: string | null,
    public fileContentType?: string | null,
    public file?: string | null
  ) {}
}

export function getToolIdentifier(tool: ITool): number | undefined {
  return tool.id;
}
