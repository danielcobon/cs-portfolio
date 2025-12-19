using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Domain
{
    public enum AttendanceStatus
    {
        Yes,
        No
    }
    public class Guest
    {
        [Key]
        public int GuestId { get; set; }

        [Required]
        [MaxLength(50)]
        public string GuestName { get; set; } = null!;

        [Required]
        public string GuestPhone { get; set; } = null!;

        public string GuestEmail { get; set; }

        [Required]
        public AttendanceStatus Attendance { get; set; }

        // Navigation property to GuestBooking (many-side of one-to-many)
        public List<GuestBooking> GuestBookings { get; set; }
    }
}
