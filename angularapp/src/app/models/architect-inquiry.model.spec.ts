import { ArchitectInquiry } from './architect-inquiry.model';

describe('ArchitectInquiryModel', () => {

  fit('frontend_Architect_Inquiry_model_should_create_an_instance', () => {
    const architectInquiry: ArchitectInquiry = {
      inquiryId: 1,
      user: {
        userId: 123,
        email: "user@example.com",
        password: "password123",
        username: "exampleUser",
        mobileNumber: "9876543210",
        userRole: "USER"
    },
    architect: {
      architectId: 456,
      name: 'John Architect',
      expertise: 'Residential Design',
      location: 'New York',
      rating: 4.5,
      portfolioLink: 'https://portfolio.example.com/johnarchitect',
      status: 'Active'
  },
      message: 'What are your rates for residential projects?',
      status: 'Pending',
      inquiryDate: '2025-01-22T10:00:00.000Z',
      priority: 'High',
      contactDetails: 'jane.doe@example.com'
    };

    expect(architectInquiry).toBeTruthy();
    expect(architectInquiry.message).toBeDefined();
    expect(architectInquiry.priority).toBeDefined();
    expect(architectInquiry.user).toBeDefined();
    expect(architectInquiry.architect).toBeDefined();
    expect(architectInquiry.status).toBeDefined();
    expect(architectInquiry.inquiryDate).toBeDefined();
    expect(architectInquiry.contactDetails).toBeDefined();
  });

});
