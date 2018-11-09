import { IEvent } from 'app/shared/model//event.model';

export interface ICategory {
    id?: number;
    name?: string;
    ids?: IEvent[];
}

export class Category implements ICategory {
    constructor(public id?: number, public name?: string, public ids?: IEvent[]) {}
}
