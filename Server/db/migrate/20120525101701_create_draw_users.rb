class CreateDrawUsers < ActiveRecord::Migration
  def change
    create_table :draw_users do |t|
      t.integer :id_draw
      t.integer :id_user

      t.timestamps
    end
  end
end
