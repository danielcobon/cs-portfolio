using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace ThAmCo.Events.Data.Migrations
{
    public partial class CascadeAddition : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_GuestBookings_Events_EventId",
                table: "GuestBookings");

            migrationBuilder.DropForeignKey(
                name: "FK_GuestBookings_Guests_GuestId",
                table: "GuestBookings");

            migrationBuilder.DropForeignKey(
                name: "FK_Staffings_Events_EventId",
                table: "Staffings");

            migrationBuilder.DropForeignKey(
                name: "FK_Staffings_Staffs_StaffId",
                table: "Staffings");

            migrationBuilder.AddForeignKey(
                name: "FK_GuestBookings_Events_EventId",
                table: "GuestBookings",
                column: "EventId",
                principalTable: "Events",
                principalColumn: "EventId",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_GuestBookings_Guests_GuestId",
                table: "GuestBookings",
                column: "GuestId",
                principalTable: "Guests",
                principalColumn: "GuestId",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_Staffings_Events_EventId",
                table: "Staffings",
                column: "EventId",
                principalTable: "Events",
                principalColumn: "EventId",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_Staffings_Staffs_StaffId",
                table: "Staffings",
                column: "StaffId",
                principalTable: "Staffs",
                principalColumn: "StaffId",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_GuestBookings_Events_EventId",
                table: "GuestBookings");

            migrationBuilder.DropForeignKey(
                name: "FK_GuestBookings_Guests_GuestId",
                table: "GuestBookings");

            migrationBuilder.DropForeignKey(
                name: "FK_Staffings_Events_EventId",
                table: "Staffings");

            migrationBuilder.DropForeignKey(
                name: "FK_Staffings_Staffs_StaffId",
                table: "Staffings");

            migrationBuilder.AddForeignKey(
                name: "FK_GuestBookings_Events_EventId",
                table: "GuestBookings",
                column: "EventId",
                principalTable: "Events",
                principalColumn: "EventId",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_GuestBookings_Guests_GuestId",
                table: "GuestBookings",
                column: "GuestId",
                principalTable: "Guests",
                principalColumn: "GuestId",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Staffings_Events_EventId",
                table: "Staffings",
                column: "EventId",
                principalTable: "Events",
                principalColumn: "EventId",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Staffings_Staffs_StaffId",
                table: "Staffings",
                column: "StaffId",
                principalTable: "Staffs",
                principalColumn: "StaffId",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
