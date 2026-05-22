INSERT INTO role (id, created_at, created_by, modified_by, updated_at, active, description, name, is_deleted) VALUES
	(3, '2025-07-12 10:37:07.913355', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:23:38.720587', true, 'Chức vụ quản trị toàn bộ hệ thống', 'ADMIN', false),
	(5, '2025-07-11  15:20:47.008697', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:23:51.777808', true, 'Người dùng bình thường, được cấp quyền tối thiểu', 'USER',false),
	(8, '2025-07-22 13:41:34.037646', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:24:10.025850', true, 'Nhà tuyển dụng, có thể truy cập trang quản lý tuyển dụng', 'RECRUITER',false);


INSERT INTO permission (id, created_at, created_by, modified_by, updated_at, api_path, method, module, name, is_deleted) VALUES
    (1,  '2025-07-12 10:29:53.249224', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:31:30.052370', '/users/me/avatar', 'PATCH', 'USER', 'Cập nhật ảnh đại diện cho người dùng hiện tại', false),
    (2,  '2025-07-12 10:30:24.113163', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:31:37.757513', '/users/me/password', 'PATCH', 'USER', 'Cập nhật mật khẩu cho người dùng hiện tại', false),
	(6,  '2025-07-12 10:29:53.249224', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:31:30.052370', '/users', 'GET', 'USER', 'Lấy danh sách User', false),
	(7,  '2025-07-12 10:30:24.113163', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:31:37.757513', '/users', 'PUT', 'USER', 'Cập nhật User', false),
	(8,  '2025-07-12 10:32:17.815563', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:32:17.815563', '/users', 'POST', 'USER', 'Tạo User', false),
	(9,  '2025-07-12 10:34:25.989081', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:34:25.989081', '/users/{id}', 'GET', 'USER', 'Tìm User theo id', false),
	(10, '2025-07-12 10:35:06.546555', 'admin@gmail.com', 'admin@gmail.com', '2025-07-12 10:35:06.546555', '/users/{id}', 'DELETE', 'USER', 'Xóa User theo id', false),

	(11, '2025-07-13 09:46:56.674094', 'admin@gmail.com', 'admin@gmail.com', '2025-07-13 09:49:24.608215', '/skills', 'POST', 'SKILL', 'Tạo Skill', false),
	(12, '2025-07-13 09:47:15.298000', 'admin@gmail.com', 'admin@gmail.com', '2025-07-13 09:47:15.298000', '/skills', 'GET', 'SKILL', 'Lấy danh sách Skill', false),
	(13, '2025-07-13 09:48:01.662749', 'admin@gmail.com', 'admin@gmail.com', '2025-07-13 09:48:01.662749', '/skills/{id}', 'GET', 'SKILL', 'Lấy Skill theo id', false),
	(14, '2025-07-13 09:48:22.758295', 'admin@gmail.com', 'admin@gmail.com', '2025-07-13 09:48:22.758295', '/skills', 'PUT', 'SKILL', 'Cập nhật Skill', false),
	(15, '2025-07-13 09:49:03.600776', 'admin@gmail.com', 'admin@gmail.com', '2025-07-13 09:49:03.600776', '/skills/{id}', 'DELETE', 'SKILL', 'Xóa Skill theo id', false),

	(16, '2025-07-16 15:11:04.499728', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:11:04.499728', '/companies', 'POST', 'COMPANY', 'Tạo Company', false),
	(17, '2025-07-16 15:11:24.271446', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:11:24.271446', '/companies/{id}', 'PUT', 'COMPANY', 'Cập nhật Company theo id', false),
	(18, '2025-07-16 15:11:44.078473', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:11:44.078473', '/companies', 'GET', 'COMPANY', 'Lấy danh sách Company', false),
	(19, '2025-07-16 15:12:06.652610', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:12:06.652610', '/companies/with-jobs-count', 'GET', 'COMPANY', 'Lấy danh sách Company kèm với số lượng nghề', false),
	(20, '2025-07-16 15:12:24.416800', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:12:24.416800', '/companies/{id}', 'GET', 'COMPANY', 'Lấy Company theo id', false),
	(21, '2025-07-16 15:12:41.177553', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:12:41.177553', '/companies/{id}', 'DELETE', 'COMPANY', 'Xóa company theo id', false),

	(22, '2025-07-16 15:39:51.862610', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:39:51.862610', '/jobs', 'POST', 'JOB', 'Tạo Job', false),
	(23, '2025-07-16 15:40:06.363460', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:40:06.363460', '/jobs/{id}', 'GET', 'JOB', 'Lấy Job theo id', false),
	(24, '2025-07-16 15:40:35.401656', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:40:35.401656', '/jobs/{id}', 'PUT', 'JOB', 'Cập nhật Job theo id', false),
	(25, '2025-07-16 15:40:57.210567', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:40:57.210567', '/jobs', 'GET', 'JOB', 'Lấy danh sách Job', false),
	(26, '2025-07-16 15:43:05.626459', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:43:05.626459', 'companies/{id}/jobs', 'GET', 'JOB', 'Lấy Job theo Company', false),
	(27, '2025-07-16 15:43:19.754552', 'admin@gmail.com', 'admin@gmail.com', '2025-07-16 15:44:30.308328', '/jobs/{id}', 'DELETE', 'JOB', 'Xóa Job theo id', false),

	(28, '2025-07-21 08:07:39.463893', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 08:07:39.463893', '/resumes', 'POST', 'RESUME', 'Tạo resume', false),
	(29, '2025-07-21 08:08:36.730444', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:09:44.361581', '/resumes/me', 'GET', 'RESUME', 'Lấy resume của người dùng hiện tại', false),
	(30, '2025-07-21 08:09:08.882486', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:21:56.318770', '/resumes/me/{id}', 'DELETE', 'RESUME', 'Xóa resume theo id của người dùng hiện tại', false),
	(31, '2025-07-21 08:10:06.631883', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:53:03.215508', '/resumes/me/{id}', 'PUT', 'RESUME', 'Cập nhật resume name của người dùng hiện tại', false),
	(32, '2025-07-21 08:10:41.125882', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 08:10:41.125882', '/resumes/me/{id}/file', 'GET', 'RESUME', 'Lấy file resume', false),
	(33, '2025-07-21 08:11:13.003269', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 08:11:13.003269', '/resumes', 'GET', 'RESUME', 'Lấy danh sách resume', false),
-- 	(34, '2025-07-21 08:11:42.249902', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 08:11:42.249902', '/resumes/{id}', 'PUT', 'RESUME', 'Cập nhật trạng thái resume', false),

	(35, '2025-07-21 09:09:09.460882', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 09:09:09.460882', '/roles', 'POST', 'ACCESS-CONTROLLER', 'Tạo Role', false),
	(36, '2025-07-21 09:09:26.844223', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 09:09:26.844223', '/roles/{id}', 'PUT', 'ACCESS-CONTROLLER', 'Cập nhật Role', false),
	(37, '2025-07-21 09:09:38.630954', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 09:09:38.630954', '/roles', 'GET', 'ACCESS-CONTROLLER', 'Lấy danh sách Role', false),
	(38, '2025-07-21 09:09:51.814997', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 09:09:51.814997', '/roles/{id}', 'DELETE', 'ACCESS-CONTROLLER', 'Xóa Role theo id', false),
	(39, '2025-07-21 10:00:44.434484', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 10:01:06.874982', '/permissions/*', 'GET', 'ACCESS-CONTROLLER', 'Thao tác quyền hạn', false),
	(40, '2025-07-21 10:02:18.773023', 'admin@gmail.com', 'admin@gmail.com', '2025-07-21 10:02:18.773023', '/admin', 'GET', 'ACCESS-CONTROLLER', 'Truy cập trang Admin', false),

	(41, '2025-07-22 05:11:07.569975', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 05:11:07.569975', '/companies/me/applications', 'GET', 'APPLICATION', 'Lấy danh sách job application theo company của người dùng hiện tại', false),
	(42, '2025-07-22 05:49:21.891262', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 05:49:21.891262', '/companies/me/applications/{id}', 'PUT', 'APPLICATION', 'Cập nhật trạng thái job application theo company của người dùng hiện tại', false),

	(43, '2025-07-22 09:00:45.719100', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 09:00:45.719100', '/companies/me/jobs', 'GET', 'JOB', 'Lấy danh sách Job theo company của người dùng hiện tại', false),
	(44, '2025-07-22 10:16:14.677636', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 10:16:14.677636', '/companies/me/jobs/{id}', 'DELETE', 'JOB', 'Xóa Job theo id thuộc company của người dùng hiện tại', false),
	(45, '2025-07-22 11:16:23.496262', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 11:16:23.496262', '/companies/me/jobs', 'POST', 'JOB', 'Tạo Job thuộc company của người dùng hiện tại', false),
	(46, '2025-07-22 11:41:31.524130', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 11:41:31.524130', '/companies/me/jobs/{id}', 'PUT', 'JOB', 'Cập nhật Job theo id thuộc company của người dùng hiện tại', false),

	(47, '2025-07-22 13:42:12.936877', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 13:42:12.936877', '/companies/me', 'GET', 'COMPANY', 'Lấy Company theo người dùng hiện tại', false),
	(48, '2025-07-22 14:13:28.808892', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 14:13:28.808892', '/companies/me', 'PUT', 'COMPANY', 'Cập nhật Company của người dùng hiện tại', false),

	(49, '2025-07-22 14:24:58.633574', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 14:24:58.633574', '/recruiter', 'GET', 'ACCESS-CONTROLLER', 'Truy cập trang Recruiter', false),

	(50, '2025-07-23 11:21:20.813320', 'admin@gmail.com', 'admin@gmail.com', '2025-07-23 11:21:20.813320', '/companies/me', 'POST', 'COMPANY', 'Tạo Company cho người dùng hiện tại', false),
	(51, '2025-07-23 13:16:19.196193', 'admin@gmail.com', 'admin@gmail.com', '2025-07-23 13:16:19.196193', '/companies/me/recruiters', 'GET', 'COMPANY', 'Lấy danh sách users recruiter của người dùng hiện tại', false),
	(52, '2025-07-23 13:48:04.623081', 'admin@gmail.com', 'admin@gmail.com', '2025-07-23 13:48:04.623081', '/companies/me/recruiters', 'POST', 'COMPANY', 'Thêm người dùng khác vào company của người dùng hiện tại', false),
	(53, '2025-07-23 14:07:45.408798', 'admin@gmail.com', 'admin@gmail.com', '2025-07-23 14:22:50.243716', '/companies/me/recruiters', 'PUT', 'COMPANY', 'Loại bỏ người dùng khác khỏi company của người dùng hiện tại', false),

	(54, '2025-07-24 05:22:00.893513', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:22:00.893513', '/subscribers/me', 'POST', 'SUBSCRIBER', 'Tạo subscriber cho người dùng hiện tại', false),
	(55, '2025-07-24 05:22:58.606915', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:22:58.606915', '/subscribers/me', 'GET', 'SUBSCRIBER', 'Lấy subscriber cho người dùng hiện tại', false),
	(56, '2025-07-24 05:23:13.620716', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:23:13.620716', '/subscribers/me', 'PUT', 'SUBSCRIBER', 'Cập nhật subscriber cho người dùng hiện tại', false),
	(57, '2025-07-24 05:23:26.077917', 'admin@gmail.com', 'admin@gmail.com', '2025-07-24 05:23:26.077917', '/subscribers/me', 'DELETE', 'SUBSCRIBER', 'Xóa subscriber cho người dùng hiện tại', false),

    (58, '2025-07-22 08:07:39.463893', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 08:07:39.463893', '/applications', 'POST', 'APPLICATION', 'Tạo job application', false),
    (59, '2025-07-22 08:08:36.730444', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:09:44.361581', '/applications/me', 'GET', 'APPLICATION', 'Lấy application của người dùng hiện tại', false),
    (60, '2025-07-22 08:09:08.882486', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:21:56.318770', '/applications/me/{jobId}', 'DELETE', 'APPLICATION', 'Xóa application theo mã công việc của người dùng hiện tại', false),
    (61, '2025-07-22 08:10:06.631883', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 03:53:03.215508', '/applications/me/{id}', 'PUT', 'APPLICATION', 'Cập nhật file resume của người dùng hiện tại', false),
    (62, '2025-07-22 08:10:41.125882', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 08:10:41.125882', '/applications/file/{id}', 'GET', 'APPLICATION', 'Lấy file resume', false),
    (63, '2025-07-22 08:11:13.003269', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 08:11:13.003269', '/applications', 'GET', 'APPLICATION', 'Lấy danh sách job application', false),
    (64, '2025-07-22 08:11:42.249902', 'admin@gmail.com', 'admin@gmail.com', '2025-07-22 08:11:42.249902', '/applications/{id}', 'PUT', 'APPLICATION', 'Cập nhật trạng thái job application', false);


	INSERT INTO roles_permissions (role_id, permission_id) VALUES
	(3, 6),
	(3, 7),
	(3, 8),
	(3, 9),
	(3, 10),
	(3, 11),
	(8, 11),
	(3, 12),
	(5, 12),
	(8, 12),
	(3, 13),
	(5, 13),
	(8, 13),
	(3, 14),
	(8, 14),
	(3, 15),
	(3, 16),
	(3, 17),
	(3, 18),
	(5, 18),
	(8, 18),
	(3, 19),
	(5, 19),
	(8, 19),
	(3, 20),
	(5, 20),
	(8, 20),
	(3, 21),
	(3, 22),
	(3, 23),
	(5, 23),
	(8, 23),
	(3, 24),
	(3, 25),
	(5, 25),
	(8, 25),
	(3, 26),
	(5, 26),
	(8, 26),
	(3, 27),
	(3, 28),
	(5, 28),
	(3, 29),
	(5, 29),
	(3, 30),
	(5, 30),
	(3, 31),
	(5, 31),
	(3, 32),
	(3, 33),
	(3, 35),
	(3, 36),
	(3, 37),
	(3, 38),
	(3, 39),
	(3, 40),
	(3, 41),
	(8, 41),
	(3, 42),
	(8, 42),
	(3, 43),
	(8, 43),
	(3, 44),
	(8, 44),
	(3, 45),
	(8, 45),
	(3, 46),
	(8, 46),
	(3, 47),
	(8, 47),
	(3, 48),
	(8, 48),
	(3, 49),
	(8, 49),
	(3, 50),
	(8, 50),
	(3, 51),
	(8, 51),
	(3, 52),
	(8, 52),
	(3, 53),
	(8, 53),
	(3, 54),
	(5, 54),
	(8, 54),
	(3, 55),
	(5, 55),
	(8, 55),
	(3, 56),
	(5, 56),
	(8, 56),
	(3, 57),
	(5, 57),
	(8, 57),
    (3, 58),
    (5, 58),
    (3, 59),
    (5, 59),
    (3, 60),
    (5, 60),
    (3, 61),
    (5, 61),
    (3, 62),
    (3, 63),
    (3, 64);

CREATE OR REPLACE FUNCTION update_company_job_count()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
UPDATE companies
SET job_count = job_count + 1
WHERE id = NEW.company_id;

ELSIF TG_OP = 'DELETE' THEN
UPDATE companies
SET job_count = job_count - 1
WHERE id = OLD.company_id;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_job_count
    AFTER INSERT OR DELETE ON jobs
    FOR EACH ROW
EXECUTE FUNCTION update_company_job_count();



INSERT INTO skills (id, name, created_at, updated_at, created_by, is_deleted) VALUES
(1, 'Java', now(), now(), 'ADMIN', false),
(2, 'SQL', now(), now(), 'ADMIN', false),
(3, 'JavaScript', now(), now(), 'ADMIN', false),
(4, 'Ruby', now(), now(), 'ADMIN', false),
(5, 'Golang', now(), now(), 'ADMIN', false),
(6, 'NextJS', now(), now(), 'ADMIN', false),
(7, 'NestJS', now(), now(), 'ADMIN', false),
(8, 'Angular', now(), now(), 'ADMIN', false),
(9, 'VueJS', now(), now(), 'ADMIN', false),
(10, 'Spring', now(), now(), 'ADMIN', false),
(11, 'Python', now(), now(), 'ADMIN', false),
(12, 'Django', now(), now(), 'ADMIN', false),
(13, 'ReactJS', now(), now(), 'ADMIN', false),
(14, 'React Native', now(), now(), 'ADMIN', false),
(15, 'Kotlin', now(), now(), 'ADMIN', false),
(16, 'Swift', now(), now(), 'ADMIN', false),
(17, 'Mobile', now(), now(), 'ADMIN', false);







