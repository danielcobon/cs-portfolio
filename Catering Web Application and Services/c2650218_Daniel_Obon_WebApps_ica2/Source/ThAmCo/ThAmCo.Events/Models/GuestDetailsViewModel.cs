using System.ComponentModel.DataAnnotations;
using System.Runtime.CompilerServices;

namespace ThAmCo.Events.Models
{
    public enum AttendanceStatus
    {
        Yes,
        No
    }
    public class GuestDetailsViewModel
    {
        [Display(Name = "Guest ID")]
        public int GuestId { get; set; }

        [Display(Name = "Guest Name")]
        public string GuestName { get; set; }

        [Display(Name = "Guest Phone Number")]
        public string GuestPhone { get; set; }

        [Display(Name = "Guest Email Address")]
        public string GuestEmail { get; set; }

        [Display(Name = "Attendance Status")]
        public AttendanceStatus Attendance { get; set; }

    }
}
