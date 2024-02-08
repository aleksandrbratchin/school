SELECT s.id, s.name, age, f.name faculty
FROM public.student s
left join public.faculty f on s.faculty_id  = f.id;

SELECT s.id, s.name, age, a.file_path
FROM public.student s
right join public.avatar a on s.id  = a.student_id;