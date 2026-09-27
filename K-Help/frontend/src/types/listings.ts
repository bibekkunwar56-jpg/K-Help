export type Job = {
  id: string;
  title: string;
  companyName: string;
  location: string;
  visaRequirements?: string | null;
  employmentType: string;
  salaryRange?: string | null;
  description: string;
  contactEmail?: string | null;
  employerNickname: string;
  employerId: string;
  createdAt: string;
};

export type House = {
  id: string;
  title: string;
  housingType: string;
  location: string;
  depositKrw: number;
  monthlyRentKrw: number;
  maintenanceFeeKrw?: number;
  floorLevel?: string | null;
  description: string;
  contactPhone?: string | null;
  landlordNickname: string;
  landlordId: string;
  createdAt: string;
};
