using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Models
{
    public class EditStaffViewModel
    {
        [Display(Name = "Staff Name")]
        public string StaffName { get; set; } = null!;

        [Display(Name = "Phone Number")]
        public string StaffPhone { get; set; }

        [Display(Name = "Email Address")]

        public string StaffEmail { get; set; }

        [Display(Name = "Staff Position")]
        public string StaffPost { get; set; }
    }
}
