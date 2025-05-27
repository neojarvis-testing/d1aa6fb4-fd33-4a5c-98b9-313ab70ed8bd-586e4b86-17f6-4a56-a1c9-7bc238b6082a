import { Feedback } from './feedback.model';

describe('FeedbackModel', () => {

    fit('frontend_Feedback_model_should_create_an_instance', () => {
    const feedback: Feedback = {
      feedbackId: 1,
      feedbackText: 'Excellent design and architecture!',
      date: '2025-01-22T10:00:00.000Z',
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
      category: 'Service'
    };

    expect(feedback).toBeTruthy();
    expect(feedback.feedbackText).toBeDefined();
    expect(feedback.date).toBeDefined();
    expect(feedback.user).toBeDefined();
    expect(feedback.architect).toBeDefined();
    expect(feedback.category).toBeDefined();
  });

});
