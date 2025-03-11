export interface Promotion {
  id: number,
  name: string,
  startsAt: Date,
  endsAt: Date,
  percentage: number,
  products: any[],
  createdAt: Date,
  updatedAt: Date,
  deletedAt: Date
}
